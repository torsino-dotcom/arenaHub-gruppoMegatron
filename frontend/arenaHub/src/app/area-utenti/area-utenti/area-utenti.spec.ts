import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AreaUtenti } from './area-utenti';

describe('AreaUtenti', () => {
  let component: AreaUtenti;
  let fixture: ComponentFixture<AreaUtenti>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AreaUtenti],
    }).compileComponents();

    fixture = TestBed.createComponent(AreaUtenti);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
