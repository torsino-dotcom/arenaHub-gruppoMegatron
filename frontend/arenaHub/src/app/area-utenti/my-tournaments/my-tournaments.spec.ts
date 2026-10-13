import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MyTournaments } from './my-tournaments';

describe('MyTournaments', () => {
  let component: MyTournaments;
  let fixture: ComponentFixture<MyTournaments>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyTournaments],
    }).compileComponents();

    fixture = TestBed.createComponent(MyTournaments);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
