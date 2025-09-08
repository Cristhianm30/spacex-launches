import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LaunchService } from '../../shared/services/launch.service';
import { LaunchSummaryResponse, LaunchPageResponse } from '../../shared/models/launch.models';
import { LaunchCard } from '../../components/launch-card/launch-card';
import { Dropdown, DropdownOption } from '../../shared/components/dropdown/dropdown';

@Component({
  selector: 'app-dashboard',
  imports: [CommonModule, LaunchCard, Dropdown],
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

  // Recent launches data
  recentLaunches: LaunchSummaryResponse[] = [];

  // Pagination properties
  currentPage = 0;
  pageSize = 9;
  totalElements = 0;
  totalPages = 0;
  selectedStatus: string | undefined = undefined;
  
  // Dropdown options for status filter
  statusOptions: DropdownOption[] = [
    { value: undefined, label: 'All Status' },
    { value: 'success', label: 'Success' },
    { value: 'failed', label: 'Failed' },
    { value: 'upcoming', label: 'Upcoming' }
  ];

  // Loading and error states
  isLoading = false;
  error: string | null = null;
  
  // Helper for template
  Math = Math;

  constructor(private launchService: LaunchService) { }

  ngOnInit(): void {
    this.loadDashboardData();
  }

  private loadDashboardData(): void {
    this.isLoading = true;
    this.error = null;

    // Load statistics
    this.launchService.getLaunchStatistics().subscribe({
      next: (statistics) => {
        console.log('Statistics received:', statistics);
        this.updateStats(statistics);
      },
      error: (error) => {
        console.error('Error isLoading statistics:', error);
        this.error = 'Failed to load launch statistics';
        this.isLoading = false;
      }
    });

    // Load paginated launches
    this.loadPaginatedLaunches();
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

  private loadPaginatedLaunches(): void {
    console.log('Loading paginated launches with params:', {
      page: this.currentPage,
      size: this.pageSize,
      status: this.selectedStatus
    });
    
    this.launchService.getPaginatedLaunches(this.currentPage, this.pageSize, this.selectedStatus).subscribe({
      next: (response: LaunchPageResponse) => {
        console.log('Paginated launches response:', response);
        this.recentLaunches = response.content;
        this.totalElements = response.totalElements;
        this.totalPages = Math.ceil(response.totalElements / response.size);
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error loading paginated launches:', error);
        this.error = 'Failed to load launches';
        this.isLoading = false;
      }
    });
  }

  // Pagination methods
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.isLoading = true;
      this.loadPaginatedLaunches();
    }
  }

  nextPage(): void {
    this.goToPage(this.currentPage + 1);
  }

  previousPage(): void {
    this.goToPage(this.currentPage - 1);
  }

  onStatusFilter(status: string | undefined): void {
    console.log('Status filter changed to:', status);
    this.selectedStatus = status;
    this.currentPage = 0; // Reset to first page
    this.isLoading = true;
    console.log('Loading paginated launches with status:', this.selectedStatus);
    this.loadPaginatedLaunches();
  }

  // Get visible page numbers for pagination
  getVisiblePages(): number[] {
    const maxVisiblePages = 5;
    const pages: number[] = [];
    
    if (this.totalPages <= maxVisiblePages) {
      // Show all pages if total is small
      for (let i = 0; i < this.totalPages; i++) {
        pages.push(i);
      }
    } else {
      // Smart pagination logic
      const halfVisible = Math.floor(maxVisiblePages / 2);
      let startPage = Math.max(0, this.currentPage - halfVisible);
      let endPage = Math.min(this.totalPages - 1, startPage + maxVisiblePages - 1);
      
      // Adjust start if we're near the end
      if (endPage - startPage < maxVisiblePages - 1) {
        startPage = Math.max(0, endPage - maxVisiblePages + 1);
      }
      
      for (let i = startPage; i <= endPage; i++) {
        pages.push(i);
      }
    }
    
    return pages;
  }

  refreshData(): void {
    this.loadDashboardData();
  }
}
