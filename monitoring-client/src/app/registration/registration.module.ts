import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RegistrationFormComponent } from './registration-form/registration-form.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { RegistrationRoutingModule } from './registration-routing.module';

@NgModule({
  declarations: [RegistrationFormComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule, RegistrationRoutingModule],
  exports: [RegistrationFormComponent],
})
export class RegistrationModule {}
