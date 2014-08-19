package com.spring.photolib.webapp.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.spring.photolib.webapp.domain.Photo;
import com.spring.photolib.webapp.domain.Rating;

public class SortHelper {

	public SortHelper() {
	}

	public String setOrderBy(String sortType) {
		String orderBy = null;
		if (sortType.equals("Alphabetical")) {
			orderBy = "order by p.name asc";

		} else if (sortType.equals("Most Recent")) {
			orderBy = "order by p.creationDate desc";
		}

		else if (sortType.equals("Top Rated")) {
			orderBy = "order by p.rating.rating desc";
		}
		return orderBy;
	}

	@Deprecated
	public void defaultSortPhoto(List<Photo> photos) {
		sortPhoto("Most Recent", photos);
	}

	@Deprecated
	public void sortPhoto(String sortType, List<Photo> photos) {
		if (photos != null && !photos.isEmpty()) {

			if (sortType.equals("Alphabetical")) {
				Collections.sort(photos, new Comparator<Photo>() {

					public int compare(Photo a, Photo b) {
						return a.getName().compareTo(b.getName());
					}

				});
			} else if (sortType.equals("Most Recent")) {
				Collections.sort(photos, new Comparator<Photo>() {

					public int compare(Photo a, Photo b) {
						return b.getCreationDate().compareTo(
								a.getCreationDate());
					}

				});
			}

			else if (sortType.equals("Top Rated")) {
				Collections.sort(photos, new Comparator<Photo>() {

					public int compare(Photo a, Photo b) {
						Rating ratingA = a.getRating();
						Rating ratingB = b.getRating();
						Float avgRatingA;
						Float avgRatingB;
						if (ratingA.getRating().equals(new Float(0.0))) {
							avgRatingA = new Float(0.0);
						} else {
							avgRatingA = ratingA.getRating()
									/ ratingA.getNumRatings();
						}
						if (ratingB.getRating().equals(new Float(0.0))) {
							avgRatingB = new Float(0.0);
						} else {
							avgRatingB = ratingB.getRating()
									/ ratingB.getNumRatings();
						}
						return avgRatingB.compareTo(avgRatingA);
					}

				});
			}
		}
	}

}
