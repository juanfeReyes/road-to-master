import { startTransition, StrictMode } from 'react'
import { createRoot, hydrateRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { HydratedRouter } from 'react-router/dom';

async function enableApiMocking() {
  const { worker } = await import('./mocks/browser.ts');

  console.info("Starting MSW service worker...");
  return worker.start();
}

enableApiMocking().then(() => {
  startTransition(() => {
    hydrateRoot(
      document,
      <StrictMode>
        <HydratedRouter />
      </StrictMode>
    );
  })
})
