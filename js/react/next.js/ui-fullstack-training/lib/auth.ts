import { betterAuth } from "better-auth";
import { admin } from 'better-auth/plugins'
import Database from 'better-sqlite3'

export const auth = betterAuth({
  emailAndPassword: {
    enabled: true
  },
  database: new Database("./sqlite.db"),
  plugins: [
    admin()
  ]
  //...
});