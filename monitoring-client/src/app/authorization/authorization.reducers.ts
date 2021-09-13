import { createReducer, on } from '@ngrx/store';
import { login, logout } from './authorization.actions';
import { User } from '../models/user';

export const initialState = {};

const _userReducer = createReducer(
  initialState,
  on(login, (state, { user }) => {
    console.log('login');
    return { ...state, user };
  }),
  on(logout, (state) => {
    console.log('login');
    return {};
  })
);

export function userReducer(state, action) {
  return _userReducer(state, action);
}
