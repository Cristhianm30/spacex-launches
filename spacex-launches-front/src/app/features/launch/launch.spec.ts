import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Launch } from './launch';

describe('Launch', () => {
  let component: Launch;
  let fixture: ComponentFixture<Launch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Launch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Launch);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
