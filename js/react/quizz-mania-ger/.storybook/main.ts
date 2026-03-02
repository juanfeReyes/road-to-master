import type { StorybookConfig } from '@storybook/nextjs-vite';
import { dirname, join } from "path";


const config: StorybookConfig = {
  "stories": [
    "../src/stories/**/*.mdx",
    "../src/stories/**/*.stories.@(js|jsx|mjs|ts|tsx)"
  ],
  "addons": [
    "@chromatic-com/storybook",
    "@storybook/addon-vitest",
    "@storybook/addon-a11y",
    "@storybook/addon-docs",
    "@storybook/addon-themes",
    "@storybook/addon-queryparams"
  ],
  "framework": "@storybook/nextjs-vite",
  "staticDirs": [
    "..\\public"
  ],
  "typescript": {
    reactDocgen: "react-docgen-typescript",
    reactDocgenTypescriptOptions: {tsconfigPath: '../tsconfig.json'}
  }
};
export default config;