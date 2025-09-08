import { TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { Dashboard } from './dashboard';
import { LaunchService } from '../../shared/services/launch.service';

describe('Dashboard', () => {
  let component: Dashboard;
  let fixture: any;
  let mockLaunchService: any;

  const mockStatistics = {
    totalLaunches: 100,
    successRate: 95.5,
    successfulLaunches: 95,
    failedLaunches: 5,
    upcomingLaunches: 10
  };

  const mockRecentLaunches = [
    { 
      launchId: '1', 
      missionName: 'Test Launch 1', 
      flightNumber: 1,
      launchDateUtc: '2023-01-01T00:00:00.000Z',
      status: 'success',
      rocketId: 'rocket1'
    },
    { 
      launchId: '2', 
      missionName: 'Test Launch 2', 
      flightNumber: 2,
      launchDateUtc: '2023-01-02T00:00:00.000Z',
      status: 'failure',
      rocketId: 'rocket2'
    }
  ];

  const mockPaginatedResponse = {
    content: mockRecentLaunches,
    number: 0,
    size: 9,
    totalElements: 2
  };

  beforeEach(async () => {
    mockLaunchService = {
      getLaunchStatistics: jest.fn(),
      getRecentLaunches: jest.fn(),
      getPaginatedLaunches: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [Dashboard],
      providers: [
        { provide: LaunchService, useValue: mockLaunchService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Dashboard);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.isLoading).toBe(false);
    expect(component.error).toBe(null);
    expect(component.recentLaunches).toEqual([]);
    expect(component.stats).toHaveLength(5);
    expect(component.currentPage).toBe(0);
    expect(component.pageSize).toBe(9);
  });

  it('should load dashboard data on init', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(mockLaunchService.getLaunchStatistics).toHaveBeenCalled();
    expect(mockLaunchService.getPaginatedLaunches).toHaveBeenCalledWith(0, 9, undefined);
  });

  it('should update stats when statistics are loaded', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(component.stats[0].value).toBe('100');
    expect(component.stats[1].value).toBe('95.5%');
    expect(component.stats[2].value).toBe('95');
    expect(component.stats[3].value).toBe('5');
    expect(component.stats[4].value).toBe('10');
  });

  it('should set recent launches when data is loaded', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(component.recentLaunches).toEqual(mockRecentLaunches);
    expect(component.isLoading).toBe(false);
  });

  it('should handle statistics error', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(throwError('Statistics error'));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(component.error).toBe('Failed to load launch statistics');
    expect(component.isLoading).toBe(false);
  });

  it('should handle recent launches error', () => {
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(throwError('Recent launches error'));

    component.ngOnInit();

    expect(component.error).toBe('Failed to load launches');
    expect(component.isLoading).toBe(false);
    expect(consoleErrorSpy).toHaveBeenCalledWith('Error loading paginated launches:', 'Recent launches error');
    
    consoleErrorSpy.mockRestore();
  });

  it('should refresh data when refreshData is called', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.refreshData();

    expect(mockLaunchService.getLaunchStatistics).toHaveBeenCalled();
    expect(mockLaunchService.getPaginatedLaunches).toHaveBeenCalledWith(0, 9, undefined);
  });

  it('should handle null statistics gracefully', () => {
    const nullStats = {
      totalLaunches: null,
      successRate: null,
      successfulLaunches: null,
      failedLaunches: null,
      upcomingLaunches: null
    };

    mockLaunchService.getLaunchStatistics.mockReturnValue(of(nullStats));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of({ content: [], number: 0, size: 9, totalElements: 0 }));

    component.ngOnInit();

    expect(component.stats[0].value).toBe('0');
    expect(component.stats[1].value).toBe('0%');
    expect(component.stats[2].value).toBe('0');
    expect(component.stats[3].value).toBe('0');
    expect(component.stats[4].value).toBe('0');
  });

  it('should handle pagination correctly', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(component.totalElements).toBe(2);
    expect(component.totalPages).toBe(1);
  });

  it('should filter launches by status', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.onStatusFilter('success');

    expect(component.selectedStatus).toBe('success');
    expect(component.currentPage).toBe(0);
    expect(mockLaunchService.getPaginatedLaunches).toHaveBeenCalledWith(0, 9, 'success');
  });
});