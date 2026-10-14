import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AdminTournamentsPageComponent } from './admin-tournaments-page.component';

describe('AdminTournamentsPageComponent', () => {
  let component: AdminTournamentsPageComponent;
  let fixture: ComponentFixture<AdminTournamentsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminTournamentsPageComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(AdminTournamentsPageComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
