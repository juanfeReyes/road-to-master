
import { faker } from '@faker-js/faker'
import styles from './BoxModel.module.scss'

export const BoxModel = () => {

  return (
    <div className={`${styles['bg-color-parent']} ${styles.padding}`}>
      <div className={`${styles.padding} ${styles['bg-color-child']} ${styles.margin} ${styles['content-outline']}`}>
        {faker.lorem.paragraph(20)}
      </div>
    </div>
  )
}
