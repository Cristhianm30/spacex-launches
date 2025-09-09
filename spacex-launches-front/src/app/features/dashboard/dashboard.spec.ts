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
    const consoleErrorSpy = jest.spyOn(console, 'error').mockImplementation();
    mockLaunchService.getLaunchStatistics.mockReturnValue(throwError('Statistics error'));
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));

    component.ngOnInit();

    expect(component.error).toBe('Failed to load launch statistics');
    expect(component.isLoading).toBe(false);
    
    consoleErrorSpy.mockRestore();
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
    const mockLargePaginatedResponse = {
      content: mockRecentLaunches,
      number: 0,
      size: 9,
      totalElements: 27
    };
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockLargePaginatedResponse));
    
    component.totalElements = 27;
    component.pageSize = 9;
    component.currentPage = 0;
    component.totalPages = Math.ceil(component.totalElements / component.pageSize);

    expect(component.totalPages).toBe(3);
    
    component.nextPage();
    expect(component.currentPage).toBe(1);
    
    component.previousPage();
    expect(component.currentPage).toBe(0);
    
    component.goToPage(2);
    expect(component.currentPage).toBe(2);
  });

  it('should filter launches by status', () => {
    mockLaunchService.getPaginatedLaunches.mockReturnValue(of(mockPaginatedResponse));
    
    component.onStatusFilter('success');
    expect(component.selectedStatus).toBe('success');
    expect(component.currentPage).toBe(0); // Should reset to first page
  });

  it('should open modal when launch card is clicked', () => {
    const launchId = 'test-launch-123';
    
    component.onLaunchCardClick(launchId);
    
    expect(component.selectedLaunchId).toBe(launchId);
    expect(component.isModalOpen).toBeTruthy();
  });

  it('should close modal when onModalClose is called', () => {
    component.selectedLaunchId = 'test-launch-123';
    component.isModalOpen = true;
    
    component.onModalClose();
    
    expect(component.selectedLaunchId).toBeNull();
    expect(component.isModalOpen).toBeFalsy();
  });

  it('should initialize modal properties correctly', () => {
    expect(component.selectedLaunchId).toBeNull();
    expect(component.isModalOpen).toBeFalsy();
  });

  it('should handle multiple modal open/close cycles', () => {
    // First launch
    component.onLaunchCardClick('launch-1');
    expect(component.selectedLaunchId).toBe('launch-1');
    expect(component.isModalOpen).toBeTruthy();
    
    // Close modal
    component.onModalClose();
    expect(component.selectedLaunchId).toBeNull();
    expect(component.isModalOpen).toBeFalsy();
    
    // Second launch
    component.onLaunchCardClick('launch-2');
    expect(component.selectedLaunchId).toBe('launch-2');
    expect(component.isModalOpen).toBeTruthy();
  });
});