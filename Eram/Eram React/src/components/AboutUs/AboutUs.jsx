import React from "react";
import styles from "./AboutUs.module.css"; // 모듈 CSS import

const AboutUs = () => (
  <div className={styles.about}>
    <div className={styles.container}>
      <div className={`${styles.about__content} ${styles.text__center}`}>
        <h2 className={`${styles.section__title} ${styles.text__cap}`}>
          about us
        </h2>
        <p className={`${styles.para__text} ${styles.text__grey}`}>
          "우리는 사람들과 장소를 연결하여
          모든 사람이 함께 멋진 일을 이루어낼 수 있도록 돕습니다.
          이것이 바로 우리의 사명입니다."
        </p>
      </div>
    </div>
  </div>
);

export default AboutUs;
