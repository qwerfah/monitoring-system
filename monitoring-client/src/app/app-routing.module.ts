import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AuthorizationFormComponent } from "./authorization/authorization-form/authorization-form.component";
import { RegistrationFormComponent } from "./registration/registration-form/registration-form.component";
import { MainFormComponent } from "./general/main-form/main-form.component";

const routes: Routes = [
  {
    path: "",
    component: MainFormComponent,
  },
  {
    path: "login",
    component: AuthorizationFormComponent,
  },
  {
    path: "register",
    component: RegistrationFormComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
