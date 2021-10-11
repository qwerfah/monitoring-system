import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatMenuModule } from '@angular/material/menu';

import { UserTableComponent } from './user-table/user-table.component';
import { CreateUserComponent } from './create-user/create-user.component';
import { AdministrationRoutingModule } from './administration-routing.module';
import { GeneralModule } from '../general/general.module';
import { PipeModule } from '../pipes/pipes.module';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    MatMenuModule,
    GeneralModule,
    PipeModule,
    AdministrationRoutingModule,
  ],
  declarations: [UserTableComponent, CreateUserComponent],
})
export class AdministrationModule {}
