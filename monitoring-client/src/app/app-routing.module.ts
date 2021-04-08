import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";

import { AuthorizationFormComponent } from "./authorization/authorization-form/authorization-form.component";
import { RegistrationFormComponent } from "./registration/registration-form/registration-form.component";

const routes: Routes = [
  {
    path: "",
    redirectTo: "login",
    pathMatch: "full",
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
