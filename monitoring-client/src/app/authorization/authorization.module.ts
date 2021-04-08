import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { AuthorizationFormComponent } from "./authorization-form/authorization-form.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [AuthorizationFormComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  exports: [AuthorizationFormComponent],
})
export class AuthorizationModule {}
