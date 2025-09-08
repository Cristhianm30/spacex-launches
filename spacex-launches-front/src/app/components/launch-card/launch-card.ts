import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LaunchSummaryResponse } from '../../shared/models/launch.models';

@Component({
  selector: 'app-launch-card',
  imports: [CommonModule],
  templateUrl: './launch-card.html',
  styleUrl: './launch-card.css'
})
export class LaunchCard {
  @Input() launch!: LaunchSummaryResponse;
}
