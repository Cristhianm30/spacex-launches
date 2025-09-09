import { Component, OnInit } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { Sidebar } from '../../sidebar/sidebar';
import { Breadcrumb, BreadcrumbItem } from '../../breadcrumb/breadcrumb';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-shell',
  imports: [RouterOutlet, Sidebar, Breadcrumb],
  templateUrl: './shell.html',
  styleUrl: './shell.css'
})
export class Shell implements OnInit {
  breadcrumbItems: BreadcrumbItem[] = [];

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.router.events
      .pipe(filter(event => event instanceof NavigationEnd))
      .subscribe((event: NavigationEnd) => {
        this.updateBreadcrumbs(event.url);
      });
    
    // Set initial breadcrumbs
    this.updateBreadcrumbs(this.router.url);
  }

  private updateBreadcrumbs(url: string): void {
    this.breadcrumbItems = [];
    
    // Home/Dashboard breadcrumb
    this.breadcrumbItems.push({
      label: 'Dashboard',
      route: '/dashboard',
      icon: 'M3 7v10a2 2 0 002 2h14a2 2 0 002-2V9a2 2 0 00-2-2H5a2 2 0 00-2-2z',
      active: url === '/dashboard' || url === '/'
    });

    // Launch detail breadcrumb
    if (url.includes('/launch/')) {
      const launchId = url.split('/launch/')[1];
      this.breadcrumbItems.push({
        label: `Launch ${launchId}`,
        route: url,
        icon: 'M19.428 15.428a2 2 0 00-1.022-.547l-2.387-.477a6 6 0 00-3.86.517l-.318.158a6 6 0 01-3.86.517L6.05 15.21a2 2 0 00-1.806.547M8 4h8l-1 1v5.172a2 2 0 00.586 1.414l5 5c1.26 1.26.367 3.414-1.415 3.414H4.828c-1.782 0-2.674-2.154-1.414-3.414l5-5A2 2 0 009 10.172V5L8 4z',
        active: true
      });
    }
  }
}
