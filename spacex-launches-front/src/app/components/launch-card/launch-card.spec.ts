import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LaunchCard } from './launch-card';
import { LaunchSummaryResponse } from '../../shared/models/launch.models';

describe('LaunchCard', () => {
  let component: LaunchCard;
  let fixture: ComponentFixture<LaunchCard>;

  const mockLaunch: LaunchSummaryResponse = {
    launchId: 'test-id',
    missionName: 'Test Mission',
    flightNumber: 1,
    launchDateUtc: '2023-01-01T00:00:00.000Z',
    status: 'success',
    rocketId: 'test-rocket'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LaunchCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LaunchCard);
    component = fixture.componentInstance;
    component.launch = mockLaunch;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display launch information', () => {
    expect(component.launch).toEqual(mockLaunch);
    expect(component.launch.missionName).toBe('Test Mission');
    expect(component.launch.status).toBe('success');
  });
});
