import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizationFormComponent } from './authorization-form/authorization-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { StoreModule } from '@ngrx/store';
import { MatSnackBarModule } from '@angular/material/snack-bar';

import { AuthorizationRoutingModule } from './authorization-routing.module';

@NgModule({
  declarations: [AuthorizationFormComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, AuthorizationRoutingModule, MatSnackBarModule],
  exports: [AuthorizationFormComponent],
})
export class AuthorizationModule {}
