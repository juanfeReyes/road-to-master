import { Banner } from "~/components/banner/Banner";
import type { Route } from "./+types/home";
import { ThemeContext } from "~/components/theme/ThemeContext";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Juan Felipe Reyes CV" },
    { name: "Juan Felipe Reyes CV", content: "Hoja de vida de Juan" },
  ];
}

export default function Home() {
  return <ThemeContext>
    <Banner />
  </ThemeContext>;
}
