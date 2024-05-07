import React from "react";
import images from "../../constants/images";
import "./AboutUs.css";

const AboutUs = () => (
    <div className='about section__padding'>
        <div className="container">
            <div className="about__content text__center">
                <h2 className="section__title text__cap">
                    about us
                </h2>
                <p className="para__text text__grey">
                "우리는 사람들과 장소 를 연결하여
                모든 사람이 함께 멋진 일을 이루어낼 수 있도록 돕습니다.
                이것이 바로 우리의 사명입니다.
                "
                </p>
            </div>
        </div>
    </div>
)

export default AboutUs; 