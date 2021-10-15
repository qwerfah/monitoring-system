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

  users: User[] = [];

  constructor(private userService: UserService, private snackBar: MatSnackBar) {}

  ngOnInit() {
    this.userService.getUsers(this.snackBar).subscribe(
      (users) => {
        this.users = users;
        this.isLoading = false;
      },
      (err) => (this.isLoading = false)
    );
  }

  openModal() {
    this.isAdding = true;
  }

  addUser(user: UserRequest | null) {
    this.isAdding = false;

    if (user === null) return;

    this.isLoading = true;

    this.userService.addUser(user, this.snackBar).subscribe(
      (user) => {
        this.isLoading = false;
        this.users.push(user);
        this.snackBar.open('Успех: пользователь создан', 'Ок', { duration: 5000 });
      },
      (err) => (this.isLoading = false)
    );
  }

  removeUser(userUid: string): void {
    if (!confirm(`Удалить пользователя ${this.users.find((u) => u.uid === userUid)?.login}?`)) return;
    this.userService.removeUser(userUid, this.snackBar).subscribe(
      (msg) => {
        this.isLoading = false;
        this.users.splice(
          this.users.findIndex((u) => u.uid === userUid),
          1
        );
        this.snackBar.open('Успех: пользователь удален', 'Ок', { duration: 5000 });
      },
      (err) => (this.isLoading = false)
    );
  }
}
