import React, { useEffect, useState } from 'react';
import { fetchSports } from '../../service/sportService';
import Grid from '@mui/material/Grid';
import images from '../../constants/images';  
import './Places.css'

const Places = () => {
    const [sports, setSports] = useState([]);

    useEffect(() => {
      const loadSports = async () => {
        try {
          const sportsData = await fetchSports();
          console.log(sportsData);
          setSports(sportsData);
        } catch (error) {
          console.error('Failed to load sports:', error);
        }
      };
  
      loadSports();
    }, []);
  
    return (
        <Grid container spacing={2} className="work__content">
            {sports.map((sport, index) => (
                <Grid item xs={12} sm={6} md={4} lg={3} key={sport.id} className="work__content--item">
                    <img src={images[`sport${index + 1}`]} alt={sport.name} />
                    <h3>{sport.name}</h3>
                </Grid>
            ))}
        </Grid>
    );
};
  
export default Places;
