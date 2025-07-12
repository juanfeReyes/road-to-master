import { Banner } from "~/components/landing/banner/Banner";
import type { Route } from "./+types/home";
import { Experiences } from "~/components/landing/experience/Experiences";
import { Certifications } from "~/components/landing/certification/Certifications";
import { Skills } from "~/components/landing/skills/Skills";
import { AboutMe } from "~/components/landing/aboutMe/AboutMe";

import styles from './home.module.scss'

export function meta({ }: Route.MetaArgs) {
  return [
    { title: "Juan Felipe Reyes CV" },
    { name: "Juan Felipe Reyes CV", content: "Hoja de vida de Juan" },
  ];
}

export default function Home() {
  return <>
    <Banner />
    <div className={`${styles.container}`}>
      <AboutMe />
      <Skills />
      <Experiences />
      <Certifications />
    </div>
  </>
}
