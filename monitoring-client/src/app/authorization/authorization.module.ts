import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizationFormComponent } from './authorization-form/authorization-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';

import { userReducer } from './authorization.reducers';
import {AuthorizationRoutingModule} from './authorization-routing.module'

@NgModule({
  declarations: [AuthorizationFormComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    AuthorizationRoutingModule,
    StoreModule.forRoot({ user: userReducer }),
  ],
  exports: [AuthorizationFormComponent],
})
export class AuthorizationModule {}
