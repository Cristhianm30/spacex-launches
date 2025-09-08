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
    { id: '1', name: 'Test Launch 1', success: true },
    { id: '2', name: 'Test Launch 2', success: false }
  ];

  beforeEach(async () => {
    mockLaunchService = {
      getLaunchStatistics: jest.fn(),
      getRecentLaunches: jest.fn()
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
    expect(component.loading).toBe(true);
    expect(component.error).toBe(null);
    expect(component.recentLaunches).toEqual([]);
    expect(component.stats).toHaveLength(5);
  });

  it('should load dashboard data on init', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getRecentLaunches.mockReturnValue(of(mockRecentLaunches));

    component.ngOnInit();

    expect(mockLaunchService.getLaunchStatistics).toHaveBeenCalled();
    expect(mockLaunchService.getRecentLaunches).toHaveBeenCalledWith(6);
  });

  it('should update stats when statistics are loaded', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getRecentLaunches.mockReturnValue(of(mockRecentLaunches));

    component.ngOnInit();

    expect(component.stats[0].value).toBe('100');
    expect(component.stats[1].value).toBe('95.5%');
    expect(component.stats[2].value).toBe('95');
    expect(component.stats[3].value).toBe('5');
    expect(component.stats[4].value).toBe('10');
  });

  it('should set recent launches when data is loaded', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getRecentLaunches.mockReturnValue(of(mockRecentLaunches));

    component.ngOnInit();

    expect(component.recentLaunches).toEqual(mockRecentLaunches);
    expect(component.loading).toBe(false);
  });

  it('should handle statistics error', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(throwError('Statistics error'));
    mockLaunchService.getRecentLaunches.mockReturnValue(of(mockRecentLaunches));

    component.ngOnInit();

    expect(component.error).toBe('Failed to load launch statistics');
    expect(component.loading).toBe(false);
  });

  it('should handle recent launches error', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getRecentLaunches.mockReturnValue(throwError('Recent launches error'));

    component.ngOnInit();

    expect(component.error).toBe('Failed to load recent launches');
    expect(component.loading).toBe(false);
  });

  it('should refresh data when refreshData is called', () => {
    mockLaunchService.getLaunchStatistics.mockReturnValue(of(mockStatistics));
    mockLaunchService.getRecentLaunches.mockReturnValue(of(mockRecentLaunches));

    component.refreshData();

    expect(mockLaunchService.getLaunchStatistics).toHaveBeenCalled();
    expect(mockLaunchService.getRecentLaunches).toHaveBeenCalledWith(6);
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
    mockLaunchService.getRecentLaunches.mockReturnValue(of([]));

    component.ngOnInit();

    expect(component.stats[0].value).toBe('0');
    expect(component.stats[1].value).toBe('0%');
    expect(component.stats[2].value).toBe('0');
    expect(component.stats[3].value).toBe('0');
    expect(component.stats[4].value).toBe('0');
  });
});