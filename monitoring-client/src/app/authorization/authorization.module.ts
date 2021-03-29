import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AuthorizationComponent } from './authorization.component';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [AuthorizationComponent],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [AuthorizationComponent]
})
export class AuthorizationModule { }
