import { Banner } from "~/components/banner/Banner";
import type { Route } from "./+types/home";
import { ThemeContext } from "~/components/theme/ThemeContext";
import { Experiences } from "~/components/experience/Experiences";
import { Certifications } from "~/components/certification/Certifications";
import { Skills } from "~/components/skills/Skills";
import { Investigations } from "~/components/Investigations/Investigations";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Juan Felipe Reyes CV" },
    { name: "Juan Felipe Reyes CV", content: "Hoja de vida de Juan" },
  ];
}

export default function Home() {
  return <ThemeContext>
    <Banner />
    <div>
      <Skills/>
      <Experiences />
      <Certifications />
      <Investigations />
    </div>
  </ThemeContext>;
}
