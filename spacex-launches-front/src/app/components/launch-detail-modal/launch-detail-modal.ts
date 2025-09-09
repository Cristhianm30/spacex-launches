import { Component, Input, Output, EventEmitter, OnInit, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LaunchResponse } from '../../shared/models/launch.models';
import { LaunchService } from '../../shared/services/launch.service';

@Component({
  selector: 'app-launch-detail-modal',
  imports: [CommonModule],
  templateUrl: './launch-detail-modal.html',
  styleUrl: './launch-detail-modal.css'
})
export class LaunchDetailModal implements OnInit, OnChanges {
  @Input() launchId: string | null = null;
  @Input() isOpen = false;
  @Output() closeModal = new EventEmitter<void>();

  launch: LaunchResponse | null = null;
  isLoading = false;
  error: string | null = null;

  constructor(private launchService: LaunchService) {}

  ngOnInit(): void {
    if (this.launchId && this.isOpen) {
      this.loadLaunchDetails();
    }
  }

  ngOnChanges(): void {
    if (this.launchId && this.isOpen && !this.launch) {
      this.loadLaunchDetails();
    }
  }

  private loadLaunchDetails(): void {
    if (!this.launchId) return;

    this.isLoading = true;
    this.error = null;

    this.launchService.getLaunchById(this.launchId).subscribe({
      next: (launch) => {
        this.launch = launch;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading launch details:', error);
        this.error = 'Failed to load launch details';
        this.isLoading = false;
      }
    });
  }

  onClose(): void {
    this.closeModal.emit();
    this.launch = null;
    this.error = null;
  }

  onBackdropClick(event: Event): void {
    if (event.target === event.currentTarget) {
      this.onClose();
    }
  }

  formatDate(dateString: string): string {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      timeZoneName: 'short'
    }) + ' UTC';
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
}
