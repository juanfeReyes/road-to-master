import * as addonQueryparams from "@storybook/addon-queryparams/preview";
import addonDocs from "@storybook/addon-docs";
import addonA11y from "@storybook/addon-a11y";
import { definePreview } from '@storybook/nextjs-vite'
import {withThemeByClassName} from '@storybook/addon-themes'
import '../app/globals.css'

export const decorators = [
  withThemeByClassName({
    themes: {
      light: 'light',
      dark: 'dark',
    },
    defaultTheme: 'light',
  }),
];

export default definePreview( {
  parameters: {
    controls: {
      matchers: {
       color: /(background|color)$/i,
       date: /Date$/i,
      },
    },

    a11y: {
      // 'todo' - show a11y violations in the test UI only
      // 'error' - fail CI on a11y violations
      // 'off' - skip a11y checks entirely
      test: 'todo'
    }
  },

  addons: [addonA11y(), addonDocs(), addonQueryparams]
});
