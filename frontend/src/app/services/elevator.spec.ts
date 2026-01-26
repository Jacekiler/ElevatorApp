import { TestBed } from '@angular/core/testing';

import { Elevator } from './elevator';

describe('Elevator', () => {
  let service: Elevator;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Elevator);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
