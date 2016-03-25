class Donation
  include Mongoid::Document
  # TODO: Change to proper type
  field :monetary_value, type: String
   validates :monetary_value,  presence: true, length: { maximum: 50 },format: { with: /\A[0-9]+\z/,
    message: "only allows Numbers" }
  field :date, type: String
   validates :date,  presence: true, length: { maximum: 50 } ,format: { with: /\d{4}\-\d{2}\-\d{2}/,
    message: "must be date and time"}
  field :type, type: String
    validates :type,  presence: true, length: { maximum: 50 } ,format: { with: /\A[a-zA-Z ]+\z/,
    message: "only allows letters and white space" }
  field :description, type: String
    validates :description,  presence: true, length: { maximum: 50 } ,format: { with: /\A[a-zA-Z ]+\z/,
    message: "only allows letters and white space" }
  has_and_belongs_to_many :persons, autosave: true

end
