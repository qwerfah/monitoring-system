import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RegistrationComponent } from "./registration.component";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";

@NgModule({
  declarations: [RegistrationComponent],
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  exports: [RegistrationComponent],
})
export class RegistrationModule {}
