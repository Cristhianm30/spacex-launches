import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LaunchService } from '../../shared/services/launch.service';
import { LaunchSummaryResponse } from '../../shared/models/launch.models';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.css'
})
export class Dashboard implements OnInit {
  // Dashboard statistics
  stats = [
    {
      title: 'Total Launches',
      value: '...',
      change: '',
      isPositive: true,
      tooltip: 'Total number of SpaceX launches ever conducted'
    },
    {
      title: 'Success Rate',
      value: '...',
      change: '',
      isPositive: true,
      tooltip: 'Percentage of successful launches'
    },
    {
      title: 'Successful Launches',
      value: '...',
      change: '',
      isPositive: true,
      tooltip: 'Number of successful SpaceX launches'
    },
    {
      title: 'Failed Launches',
      value: '...',
      change: '',
      isPositive: false,
      tooltip: 'Number of failed SpaceX launches'
    },
    {
      title: 'Upcoming Launches',
      value: '...',
      change: '',
      isPositive: true,
      tooltip: 'Number of upcoming SpaceX launches scheduled'
    }
  ];

  recentLaunches: LaunchSummaryResponse[] = [];
  loading = true;
  error: string | null = null;

  constructor(private launchService: LaunchService) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.loading = true;
    this.error = null;

    // Load statistics
    this.launchService.getLaunchStatistics().subscribe({
      next: (statistics) => {
        console.log('Statistics received:', statistics);
        this.updateStats(statistics);
      },
      error: (error) => {
        console.error('Error loading statistics:', error);
        this.error = 'Failed to load launch statistics';
        this.loading = false;
      }
    });

    // Load recent launches
    this.launchService.getRecentLaunches(6).subscribe({
      next: (launches) => {
        this.recentLaunches = launches;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error loading recent launches:', error);
        this.error = 'Failed to load recent launches';
        this.loading = false;
      }
    });
  }

  private updateStats(statistics: any): void {
    console.log('Updating stats with:', statistics);
    this.stats = [
      {
        title: 'Total Launches',
        value: statistics.totalLaunches?.toString() || '0',
        change: '',
        isPositive: true,
        tooltip: 'Total number of SpaceX launches ever conducted'
      },
      {
        title: 'Success Rate',
        value: statistics.successRate ? `${statistics.successRate.toFixed(1)}%` : '0%',
        change: '',
        isPositive: (statistics.successRate || 0) > 90,
        tooltip: 'Percentage of successful launches'
      },
      {
        title: 'Successful Launches',
        value: statistics.successfulLaunches?.toString() || '0',
        change: '',
        isPositive: true,
        tooltip: 'Number of successful SpaceX launches'
      },
      {
        title: 'Failed Launches',
        value: statistics.failedLaunches?.toString() || '0',
        change: '',
        isPositive: false,
        tooltip: 'Number of failed SpaceX launches'
      },
      {
        title: 'Upcoming Launches',
        value: statistics.upcomingLaunches?.toString() || '0',
        change: '',
        isPositive: true,
        tooltip: 'Number of upcoming SpaceX launches scheduled'
      }
    ];
    console.log('Stats updated to:', this.stats);
  }

  refreshData(): void {
    this.loadDashboardData();
  }
}
