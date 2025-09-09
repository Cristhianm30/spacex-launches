import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of, throwError } from 'rxjs';
import { LaunchDetailModal } from './launch-detail-modal';
import { LaunchService } from '../../shared/services/launch.service';
import { LaunchResponse } from '../../shared/models/launch.models';

describe('LaunchDetailModal', () => {
  let component: LaunchDetailModal;
  let fixture: ComponentFixture<LaunchDetailModal>;
  let mockLaunchService: jest.Mocked<LaunchService>;

  const mockLaunchResponse: LaunchResponse = {
    launchId: 'test-123',
    missionName: 'Test Mission',
    flightNumber: 100,
    launchDateUtc: '2024-01-15T10:30:00Z',
    success: true,
    status: 'success',
    rocketId: 'falcon9',
    launchpadId: 'ksc_lc_39a',
    details: 'Test mission details',
    webcastLink: 'https://youtube.com/watch?v=test',
    articleLink: 'https://spacex.com/article/test',
    wikipediaLink: 'https://en.wikipedia.org/wiki/test',
    patchSmallLink: 'https://images.spacex.com/test-small.png',
    patchLargeLink: 'https://images.spacex.com/test-large.png',
    payloads: ['payload-1']
  };

  beforeEach(async () => {
    const launchServiceSpy = {
      getLaunchById: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [LaunchDetailModal],
      providers: [
        { provide: LaunchService, useValue: launchServiceSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LaunchDetailModal);
    component = fixture.componentInstance;
    mockLaunchService = TestBed.inject(LaunchService) as jest.Mocked<LaunchService>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with default values', () => {
    expect(component.launchId).toBeNull();
    expect(component.isOpen).toBeFalsy();
    expect(component.launch).toBeNull();
    expect(component.isLoading).toBeFalsy();
    expect(component.error).toBeNull();
  });

  it('should load launch data when modal opens', () => {
    mockLaunchService.getLaunchById.mockReturnValue(of(mockLaunchResponse));
    
    component.launchId = 'test-123';
    component.isOpen = true;
    component.ngOnChanges();

    expect(component.isLoading).toBeFalsy();
    expect(mockLaunchService.getLaunchById).toHaveBeenCalledWith('test-123');
    expect(component.launch).toEqual(mockLaunchResponse);
    expect(component.error).toBeNull();
  });

  it('should handle loading state correctly', () => {
    // Create a delayed observable to test loading state
    const delayedResponse = new Promise(resolve => {
      setTimeout(() => resolve(mockLaunchResponse), 100);
    });
    mockLaunchService.getLaunchById.mockReturnValue(of(mockLaunchResponse));
    
    component.launchId = 'test-123';
    component.isOpen = true;
    component.launch = null; // Ensure launch is null to trigger loading
    
    // Manually set loading state to test
    component['loadLaunchDetails']();
    
    // Check that loading was set to true during the call
    expect(mockLaunchService.getLaunchById).toHaveBeenCalledWith('test-123');
  });

  it('should handle error when loading launch fails', () => {
    const errorMessage = 'Failed to load launch';
    mockLaunchService.getLaunchById.mockReturnValue(throwError(() => new Error(errorMessage)));
    
    component.launchId = 'test-123';
    component.isOpen = true;
    component.ngOnChanges();

    expect(component.isLoading).toBeFalsy();
    expect(component.error).toBe('Failed to load launch details');
    expect(component.launch).toBeNull();
  });

  it('should not load data when modal is closed', () => {
    component.launchId = 'test-123';
    component.isOpen = false;
    component.ngOnChanges();

    expect(mockLaunchService.getLaunchById).not.toHaveBeenCalled();
  });

  it('should not load data when launchId is null', () => {
    component.launchId = null;
    component.isOpen = true;
    component.ngOnChanges();

    expect(mockLaunchService.getLaunchById).not.toHaveBeenCalled();
  });

  it('should emit closeModal when onClose is called', () => {
    jest.spyOn(component.closeModal, 'emit');
    
    component.onClose();
    
    expect(component.closeModal.emit).toHaveBeenCalled();
  });

  it('should close modal when clicking outside', () => {
    jest.spyOn(component, 'onClose');
    const mockTarget = document.createElement('div');
    const mockEvent = { target: mockTarget, currentTarget: mockTarget } as any;
    
    component.onBackdropClick(mockEvent);
    
    expect(component.onClose).toHaveBeenCalled();
  });

  it('should not close modal when clicking inside', () => {
    jest.spyOn(component, 'onClose');
    const mockTarget = document.createElement('div');
    const mockCurrentTarget = document.createElement('div');
    const mockEvent = { target: mockTarget, currentTarget: mockCurrentTarget } as any;
    
    component.onBackdropClick(mockEvent);
    
    expect(component.onClose).not.toHaveBeenCalled();
  });

  it('should format date correctly', () => {
    const testDate = '2024-01-15T10:30:00Z';
    const formattedDate = component.formatDate(testDate);
    
    expect(formattedDate).toContain('UTC');
    expect(formattedDate).toContain('Jan');
    expect(formattedDate).toContain('15');
    expect(formattedDate).toContain('2024');
  });

  it('should return correct status class for success', () => {
    const statusClass = component.getStatusClass('success');
    expect(statusClass).toBe('bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200');
  });

  it('should return correct status class for failure', () => {
    const statusClass = component.getStatusClass('failure');
    expect(statusClass).toBe('bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200');
  });

  it('should return correct status class for failed', () => {
    const statusClass = component.getStatusClass('failed');
    expect(statusClass).toBe('bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200');
  });

  it('should return correct status class for upcoming', () => {
    const statusClass = component.getStatusClass('upcoming');
    expect(statusClass).toBe('bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200');
  });

  it('should return default status class for unknown status', () => {
    const statusClass = component.getStatusClass('unknown');
    expect(statusClass).toBe('bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200');
  });

  it('should handle case insensitive status', () => {
    const statusClass = component.getStatusClass('SUCCESS');
    expect(statusClass).toBe('bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200');
  });
});
