import { createReducer, on } from '@ngrx/store';
import { login, logout } from './authorization.actions';
import { User } from '../models/user';

export const initialState = {};

const _userReducer = createReducer(
  initialState,
  on(login, (state, { creds }) => {
    console.log('login');
    return { ...state, creds };
  }),
  on(logout, (state) => {
    console.log('login');
    return {};
  })
);

export function userReducer(state, action) {
  return _userReducer(state, action);
}
