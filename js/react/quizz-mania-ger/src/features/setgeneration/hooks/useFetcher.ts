import { Domain } from "@/src/features/common/model/Domain";
import { FileConfig } from "@/src/features/common/model/Question";
import { load } from 'js-yaml'
import { useGameStore } from "../../common/store/GameStore";


export const useFetcher = () => {

    const setConfig = useGameStore((state) => state.setConfig)

    const fetchDomains = async (files: FileConfig[]): Promise<Domain[]> => {
        const questions = await files.map(async (file) =>
            await (await fetch(`/assets/domains/${file.file}`)).text()
        )
        const results: Domain[] = (await Promise.all(questions)).map(quiz => load(quiz))
        return results
    }

    const setConfigManifest = async () => {
        const manifest = await (await fetch('/assets/domains/quiz-manifest.json')).json()
        setConfig(manifest)
    }

    return { fetchDomains, setConfigManifest }
}


