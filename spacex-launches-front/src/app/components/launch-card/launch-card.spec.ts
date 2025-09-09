import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LaunchCard } from './launch-card';
import { LaunchSummaryResponse } from '../../shared/models/launch.models';

describe('LaunchCard', () => {
  let component: LaunchCard;
  let fixture: ComponentFixture<LaunchCard>;

  const mockLaunch: LaunchSummaryResponse = {
    launchId: 'test-123',
    missionName: 'Test Mission',
    flightNumber: 100,
    launchDateUtc: '2024-01-15T10:30:00Z',
    status: 'success',
    rocketId: 'falcon9'
  };

  const mockLongNameLaunch: LaunchSummaryResponse = {
    launchId: 'test-456',
    missionName: 'This is a very long mission name that exceeds thirty characters',
    flightNumber: 101,
    launchDateUtc: '2024-01-16T10:30:00Z',
    status: 'upcoming',
    rocketId: 'falcon9'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LaunchCard]
    }).compileComponents();

    fixture = TestBed.createComponent(LaunchCard);
    component = fixture.componentInstance;
    component.launch = mockLaunch;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display launch information', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('Test Mission');
    expect(compiled.textContent).toContain('Flight #100');
  });

  it('should emit cardClick when card is clicked', () => {
    jest.spyOn(component.cardClick, 'emit');
    
    component.onCardClick();
    
    expect(component.cardClick.emit).toHaveBeenCalledWith('test-123');
  });

  it('should format date correctly', () => {
    const testDate = '2024-01-15T10:30:00Z';
    const formattedDate = component.formatDate(testDate);
    
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

  it('should return full mission name when under 30 characters', () => {
    const truncatedName = component.getTruncatedMissionName();
    expect(truncatedName).toBe('Test Mission');
  });

  it('should truncate mission name when over 30 characters', () => {
    component.launch = mockLongNameLaunch;
    const truncatedName = component.getTruncatedMissionName();
    
    expect(truncatedName).toBe('This is a very long mission na...');
    expect(truncatedName.length).toBe(33); // 30 chars + '...'
  });

  it('should trigger card click when clicked in template', () => {
    jest.spyOn(component, 'onCardClick');
    
    const cardElement = fixture.nativeElement.querySelector('[data-testid="launch-card"]') || 
                       fixture.nativeElement.querySelector('div');
    cardElement.click();
    
    expect(component.onCardClick).toHaveBeenCalled();
  });

  it('should display truncated mission name in template', () => {
    component.launch = mockLongNameLaunch;
    fixture.detectChanges();
    
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.textContent).toContain('This is a very long mission na...');
  });

  it('should show full mission name in title attribute', () => {
    component.launch = mockLongNameLaunch;
    fixture.detectChanges();
    
    const titleElement = fixture.nativeElement.querySelector('h3');
    expect(titleElement?.getAttribute('title')).toBe('This is a very long mission name that exceeds thirty characters');
  });
});
