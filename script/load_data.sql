
COPY users ( user_id , password , first_name , last_name , e_mail , street1 , state , country , zipcode , balance)
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/users.data' 
WITH DELIMITER ';';
 
COPY super_user ( super_user_id )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/superusers.data' 
WITH DELIMITER ';';

COPY follow (user_id_to , user_id_from , follow_time)
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/followers.data' 
WITH DELIMITER ';';

COPY video ( video_id , title , year, online_price, dvd_price)
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/video.data' 
WITH DELIMITER ';';

COPY genre ( genre_id , genre_name )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/genre.data' 
WITH DELIMITER ';';

COPY categorize ( video_id , genre_id )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/categorize.data' 
WITH DELIMITER ';';

COPY director ( director_id , first_name , last_name )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/director.data' 
WITH DELIMITER ';';

COPY star ( star_id , first_name , last_name )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/stars.data' 
WITH DELIMITER ';';

COPY author ( author_id , first_name , last_name )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/authors.data' 
WITH DELIMITER ';';

COPY directed ( video_id , director_id)
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/directed.data' 
WITH DELIMITER ';';

COPY played ( video_id, star_id)
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/played.data' 
WITH DELIMITER ';';

COPY written ( video_id , author_id )
FROM '/home/csmajs/wongc/Desktop/cs166project/dataset/written.data' 
WITH DELIMITER ';';

