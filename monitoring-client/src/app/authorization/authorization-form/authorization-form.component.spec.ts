import { ComponentFixture, TestBed } from "@angular/core/testing";

import { AuthorizationFormComponent } from "./authorization-form.component";

describe("AuthorizationComponent", () => {
  let component: AuthorizationFormComponent;
  let fixture: ComponentFixture<AuthorizationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AuthorizationFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(AuthorizationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });
});
