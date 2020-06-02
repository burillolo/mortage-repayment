import { TestBed } from '@angular/core/testing';

import { CuotasServiceService } from './cuotas-service.service';

describe('CuotasServiceService', () => {
  let service: CuotasServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CuotasServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
