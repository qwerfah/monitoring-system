import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { v4 as uuid } from 'uuid';

import { User } from 'src/app/models/user';
import { UserRequest } from 'src/app/models/user-request';
import { UserService } from 'src/app/services/user.service';
import { UserRole } from 'src/app/models/user-role';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-user-table',
  templateUrl: './user-table.component.html',
  styleUrls: ['./user-table.component.css'],
})
export class UserTableComponent implements OnInit {
  isLoading: boolean = true;
  isAdding: boolean = false;

  users: User[];

  constructor(private userService: UserService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.users = [
      new User(uuid(), 'user_1', UserRole.EquipmentUser),
      new User(uuid(), 'user_2', UserRole.SystemAdmin),
      new User(uuid(), 'user_3', UserRole.EquipmentAdmin),
      new User(uuid(), 'user_4', UserRole.EquipmentUser),
    ];

    this.userService.getUsers().subscribe(
      (users) => {
        this.users = users;
        this.isLoading = false;
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис пользователей недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );
  }

  openModal() {
    this.isAdding = true;
  }

  addUser(user: UserRequest | null) {
    this.isAdding = false;

    if (user === null) return;

    this.isLoading = true;

    this.userService.addUser(user).subscribe(
      (user) => {
        this.isLoading = false;
        this.users.push(user);
        this.snackBar.open('Успех: пользователь создан', 'Ок', { duration: 5000 });
      },
      (err: HttpErrorResponse) => {
        switch (err.status) {
          case 0: {
            this.snackBar.open('Ошибка: отсутсвтует соединение с сервером', 'Ок', { duration: 5000 });
            break;
          }
          case 502: {
            this.snackBar.open('Ошибка: сервис пользователей недоступен', 'Ок', { duration: 5000 });
            break;
          }
          case 404: {
            this.snackBar.open('Ошибка: данные не найдены', 'Ок', { duration: 5000 });
          }
        }
        this.isLoading = false;
      }
    );
  }

  removeUser(userUid: string): void {
    console.log(userUid);
  }
}
