import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HomeTournamentsPreviewComponent } from './home-tournaments-preview.component';

describe('HomeTournamentsPreviewComponent', () => {
  let component: HomeTournamentsPreviewComponent;
  let fixture: ComponentFixture<HomeTournamentsPreviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HomeTournamentsPreviewComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(HomeTournamentsPreviewComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
