import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Elevator } from './elevator';

describe('Elevator', () => {
  let component: Elevator;
  let fixture: ComponentFixture<Elevator>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Elevator]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Elevator);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
