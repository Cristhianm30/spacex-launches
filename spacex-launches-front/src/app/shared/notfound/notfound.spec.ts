import { TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { Notfound } from './notfound';

describe('Notfound', () => {
  let component: Notfound;
  let fixture: any;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Notfound],
      providers: [provideRouter([])]
    }).compileComponents();

    fixture = TestBed.createComponent(Notfound);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});