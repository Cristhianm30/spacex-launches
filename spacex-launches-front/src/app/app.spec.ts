import { TestBed } from '@angular/core/testing';
import { Router, NavigationEnd } from '@angular/router';
import { provideRouter } from '@angular/router';
import { Subject } from 'rxjs';
import { App } from './app';

describe('App', () => {
  let component: App;
  let fixture: any;
  let router: Router;
  let routerEvents: Subject<any>;

  beforeEach(async () => {
    routerEvents = new Subject();
    
    // Mock HSStaticMethods
    (window as any).HSStaticMethods = {
      autoInit: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [App],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(App);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
    
    // Mock router events
    jest.spyOn(router, 'events', 'get').mockReturnValue(routerEvents);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call HSStaticMethods.autoInit on NavigationEnd', () => {
    jest.useFakeTimers();
    
    component.ngOnInit();
    
    routerEvents.next(new NavigationEnd(1, '/', '/'));
    
    jest.advanceTimersByTime(100);
    
    expect(window.HSStaticMethods.autoInit).toHaveBeenCalled();
    
    jest.useRealTimers();
  });

  it('should not call HSStaticMethods.autoInit on other events', () => {
    component.ngOnInit();
    
    routerEvents.next({ type: 'other-event' });
    
    expect(window.HSStaticMethods.autoInit).not.toHaveBeenCalled();
  });
});