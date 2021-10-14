/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AddParamComponent } from './add-param.component';

describe('AddParamComponent', () => {
  let component: AddParamComponent;
  let fixture: ComponentFixture<AddParamComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddParamComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddParamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
