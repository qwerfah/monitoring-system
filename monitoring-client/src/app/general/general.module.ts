import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { MainFormComponent } from "./main-form/main-form.component";

@NgModule({
  declarations: [MainFormComponent],
  imports: [CommonModule],
  exports: [MainFormComponent],
})
export class GeneralModule {}
