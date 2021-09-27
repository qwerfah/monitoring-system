import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// import { AuthorizationFormComponent } from './authorization/authorization-form/authorization-form.component';
// import { RegistrationFormComponent } from './registration/registration-form/registration-form.component';
// import { MainFormComponent } from './general/main-form/main-form.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: '/',
    pathMatch: 'full'
  },
  {
    path: 'login',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'register',
    redirectTo: 'register',
    pathMatch: 'full'
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
