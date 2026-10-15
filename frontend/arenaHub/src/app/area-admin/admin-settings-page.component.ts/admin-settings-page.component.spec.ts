import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminSettingsPageComponent } from './admin-settings-page.component.js';

describe('AdminSettingsPageComponentTs', () => {
  let component: AdminSettingsPageComponent;
  let fixture: ComponentFixture<AdminSettingsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminSettingsPageComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminSettingsPageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
