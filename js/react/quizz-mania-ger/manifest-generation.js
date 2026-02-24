import fs from 'fs'
import {load} from 'js-yaml'

export function buildManifest() {
    const quizzPath = './public/assets/domains'
    const manifestPath = `${quizzPath}/quiz-manifest.json`

    fs.rmSync(manifestPath, {force: true})

    const langs = fs.readdirSync(quizzPath);
    let manifest = {}

    langs.forEach(lang => {
        manifest = { ...manifest, [lang]: [] }
        const domains = fs.readdirSync(`${quizzPath}/${lang}`);
        domains.forEach(dom => {
            const quizzes = fs.readdirSync(`${quizzPath}/${lang}/${dom}`);
            quizzes.forEach(quiz => {
                const data = fs.readFileSync(`${quizzPath}/${lang}/${dom}/${quiz}`)
                const json = {...load(data), file: `/${lang}/${dom}/${quiz}`}
                delete json['quiz']
                manifest[lang].push(json)

            })
        })
    })

    fs.writeFileSync(manifestPath, JSON.stringify(manifest, null, 4))
}

buildManifest()