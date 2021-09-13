import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizationFormComponent } from './authorization-form/authorization-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';
import { userReducer } from './authorization.reducers';

@NgModule({
  declarations: [AuthorizationFormComponent],
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    StoreModule.forRoot({ user: userReducer }),
  ],
  exports: [AuthorizationFormComponent],
})
export class AuthorizationModule {}
