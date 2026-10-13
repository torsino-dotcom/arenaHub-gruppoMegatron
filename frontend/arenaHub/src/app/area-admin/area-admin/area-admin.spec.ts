import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AreaAdmin } from './area-admin';

describe('AreaAdmin', () => {
  let component: AreaAdmin;
  let fixture: ComponentFixture<AreaAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AreaAdmin],
    }).compileComponents();

    fixture = TestBed.createComponent(AreaAdmin);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
