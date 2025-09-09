import { Component, Input, Output, EventEmitter } from '@angular/core';
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
  @Output() cardClick = new EventEmitter<string>();

  onCardClick(): void {
    this.cardClick.emit(this.launch.launchId);
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  getStatusClass(status: string): string {
    switch (status.toLowerCase()) {
      case 'success':
        return 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200';
      case 'failure':
      case 'failed':
        return 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200';
      case 'upcoming':
        return 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200';
      default:
        return 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200';
    }
  }

  getTruncatedMissionName(): string {
    if (this.launch.missionName.length > 30) {
      return this.launch.missionName.substring(0, 30) + '...';
    }
    return this.launch.missionName;
  }
}
